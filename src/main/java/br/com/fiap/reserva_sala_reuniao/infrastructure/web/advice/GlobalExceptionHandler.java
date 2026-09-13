package br.com.fiap.reserva_sala_reuniao.infrastructure.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.reserva_sala_reuniao.infrastructure.web.dtos.ErroRespostaDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta violações de regras de negócio e permissões negadas.
     * Mapeado para HTTP 403 Forbidden.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroRespostaDTO> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        
        ErroRespostaDTO erro = ErroRespostaDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro("Ação Não Permitida")
                .mensagem(ex.getMessage())
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(erro);
    }

    /**
     * Intercepta dados inválidos enviados na requisição (Ex: IDs inexistentes ou datas incorretas).
     * Mapeado para HTTP 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRespostaDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErroRespostaDTO erro = ErroRespostaDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro("Requisição Inválida")
                .mensagem(ex.getMessage())
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(erro);
    }

    /**
     * Intercepta qualquer outro erro inesperado do sistema (Ex: queda de banco de dados).
     * Mapeado para HTTP 500 Internal Server Error para não expor detalhes técnicos ao cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroRespostaDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErroRespostaDTO erro = ErroRespostaDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro("Erro Interno do Servidor")
                .mensagem("Ocorreu um erro inesperado no sistema. Tente novamente mais tarde.")
                .caminho(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(erro);
    }
}
