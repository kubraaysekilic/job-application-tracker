package dev.portfolio.tracker;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController @RequestMapping("/api/applications")
public class ApplicationController {
 private final ApplicationRepository repository;
 public ApplicationController(ApplicationRepository repository){this.repository=repository;}
 @GetMapping public List<ApplicationRecord> list(){return repository.findAll(Sort.by(Sort.Direction.DESC,"appliedOn","id"));}
 @PostMapping @ResponseStatus(HttpStatus.CREATED)
 public ApplicationRecord create(@Valid @RequestBody ApplicationInput input){
  var record=new ApplicationRecord();apply(record,input);return repository.saveAndFlush(record);
 }
 @PutMapping("/{id}") @Transactional
 public ApplicationRecord update(@PathVariable Long id,@Valid @RequestBody ApplicationInput input){
  var record=find(id);
  if(input.version()==null || !Objects.equals(record.version,input.version()))
   throw new ResponseStatusException(HttpStatus.CONFLICT,"Kayıt değişmiş. Listeyi yenileyip tekrar dene.");
  apply(record,input);return repository.saveAndFlush(record);
 }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Transactional
 public void delete(@PathVariable Long id,@RequestParam Long version){
  var record=find(id);
  if(!Objects.equals(record.version,version))throw new ResponseStatusException(HttpStatus.CONFLICT,"Kayıt değişmiş. Listeyi yenile.");
  repository.delete(record);repository.flush();
 }
 private ApplicationRecord find(Long id){return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Başvuru bulunamadı."));}
 private void apply(ApplicationRecord target,ApplicationInput input){
  String url=input.url()==null?"":input.url().trim();
  if(!url.isEmpty())try{
   var uri=URI.create(url);
   if(!List.of("https","http").contains(uri.getScheme()) || uri.getHost()==null || uri.getUserInfo()!=null)throw new IllegalArgumentException();
  }catch(IllegalArgumentException exception){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"İlan bağlantısı geçerli bir HTTP/HTTPS adresi olmalı.");}
  target.company=input.company().trim();target.position=input.position().trim();target.url=url;
  target.appliedOn=input.appliedOn();target.status=input.status();target.notes=input.notes()==null?"":input.notes().trim();
 }
}
