package cooperativa.controller;

import cooperativa.model.IdNaoEncontradoException;
import cooperativa.model.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(IdNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> idNaoEncontrado(IdNaoEncontradoException exception){
        ErroResponse erro = new ErroResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> regraDeNegocio(RegraDeNegocioException exception){
        ErroResponse erro = new ErroResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return  ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> escritaErrada(MethodArgumentNotValidException exception){
        List<String> escrita = new ArrayList<String>();
        for (FieldError errada : exception.getBindingResult().getFieldErrors()){
            escrita.add(errada.getField() + ": " + errada.getDefaultMessage());
        }
        ErroResponse erro = new ErroResponse("Os dados da requisição foram inválidos",HttpStatus.BAD_REQUEST.value(), escrita);
        return ResponseEntity.badRequest().body(erro);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException exception){
        ErroResponse erro = new ErroResponse("O corpo da requisição está com um formato ruim ou tem um valor inválido.", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> parametroFaltando(MissingServletRequestParameterException exception){
        ErroResponse erro = new ErroResponse("O parâmetro '" + exception.getParameterName() + "' é obrigatório.", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(erro);
    }

}