package org.example.rikkeibank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.example.rikkeibank.dto.request.TransferRequest;
import org.example.rikkeibank.dto.response.TransferResponse;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditLogAspect {

    @AfterReturning(
            pointcut = "execution(* org.example.rikkeibank.service.impl.TransferServiceImpl.transfer(..))",
            returning = "result"
    )
    public void logTransferSuccess(JoinPoint joinPoint, Object result) {
        if (result instanceof TransferResponse response) {
            log.info("Transfer success: {} -> {}, amount={}, description={}",
                    response.getFromAccount(),
                    response.getToAccount(),
                    response.getAmount(),
                    response.getDescription()
            );
        } else {
            log.info("Transfer success");
        }
    }

    @AfterThrowing(
            pointcut = "execution(* org.example.rikkeibank.service.impl.TransferServiceImpl.transfer(..))",
            throwing = "ex"
    )
    public void logTransferFailure(JoinPoint joinPoint, Throwable ex) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof TransferRequest request) {
            log.warn("Transfer failed: toAccount={}, amount={}, reason={}",
                    request.getToAccountNumber(),
                    request.getAmount(),
                    ex.getMessage()
            );
        } else {
            log.warn("Transfer failed: reason={}", ex.getMessage());
        }
    }
}
