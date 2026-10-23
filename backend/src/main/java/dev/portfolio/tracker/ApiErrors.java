package dev.portfolio.tracker;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.util.Map;
@RestControllerAdvice
public class ApiErrors {
 @ExceptionHandler(ResponseStatusException.class)
 ResponseEntity<Map<String,String>> status(ResponseStatusException error){return ResponseEntity.status(error.getStatusCode()).body(Map.of("message",error.getReason()==null?"İşlem başarısız.":error.getReason()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException error){
  var field=error.getBindingResult().getFieldErrors().get(0);
  return ResponseEntity.badRequest().body(Map.of("message",field.getField()+": "+field.getDefaultMessage()));
 }
 @ExceptionHandler(HttpMessageNotReadableException.class)
 ResponseEntity<Map<String,String>> unreadable(){return ResponseEntity.badRequest().body(Map.of("message","Alanların biçimini kontrol et."));}
 @ExceptionHandler(OptimisticLockingFailureException.class)
 ResponseEntity<Map<String,String>> conflict(){return ResponseEntity.status(409).body(Map.of("message","Kayıt başka bir işlem tarafından değiştirildi. Listeyi yenile."));}
}
