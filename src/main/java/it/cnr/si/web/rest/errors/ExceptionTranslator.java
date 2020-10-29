/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.web.rest.errors;

import it.cnr.si.web.rest.util.HeaderUtil;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
            .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE : problem.getType())
            .withStatus(problem.getStatus())
            .withTitle(problem.getTitle())
            .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder
                .with("violations", ((ConstraintViolationProblem) problem).getViolations())
                .with("message", ErrorConstants.ERR_VALIDATION);
        } else {
            builder
                .withCause(((DefaultProblem) problem).getCause())
                .withDetail(problem.getDetail())
                .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey("message") && problem.getStatus() != null) {
                builder.with("message", "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
            .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode()))
            .collect(Collectors.toList());

        Problem problem = Problem.builder()
            .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
            .withTitle("Method argument not valid")
            .withStatus(defaultConstraintViolationStatus())
            .with("message", ErrorConstants.ERR_VALIDATION)
            .with("fieldErrors", fieldErrors)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.NOT_FOUND)
            .with("message", ErrorConstants.ENTITY_NOT_FOUND_TYPE)
            .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(ex.getEntityName(), ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
            .withStatus(Status.CONFLICT)
            .with("message", ErrorConstants.ERR_CONCURRENCY_FAILURE)
            .build();
        return create(ex, problem, request);
    }
}
