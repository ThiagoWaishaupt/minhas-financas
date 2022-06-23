package com.thiago.minhasfinancas.resource;

import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.ReleaseDTO;
import com.thiago.minhasfinancas.model.ReleaseStatusDTO;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.model.enums.ReleaseStatus;
import com.thiago.minhasfinancas.model.enums.ReleaseType;
import com.thiago.minhasfinancas.service.ReleaseService;
import com.thiago.minhasfinancas.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/releases")
@RequiredArgsConstructor
public class ReleaseResource {

    private ReleaseService releaseService;
    private UserService userService;

    @PostMapping
    public ResponseEntity save(@RequestBody ReleaseDTO releaseDTO){
        try {
            Release release = convert(releaseDTO);
            release = releaseService.save(release);
            return new ResponseEntity(release, HttpStatus.CREATED);
        } catch (BusinessRuleException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody ReleaseDTO releaseDTO){
        return releaseService.searchReleaseById(id).map(result -> {
            try{
                Release release = convert(releaseDTO);
                release.setId(result.getId());
                releaseService.update(release);
                return new ResponseEntity(release, HttpStatus.OK);
            } catch (BusinessRuleException e){
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        }).orElseGet(() -> new ResponseEntity("Release not found", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        return releaseService.searchReleaseById(id).map(result -> {
            releaseService.delete(result);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet( () -> new ResponseEntity("Release not found", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity search(
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam("user") Long idUser
    ){
        Release release = Release.builder()
                .description(description)
                .year(year)
                .month(month)
                .releaseType(ReleaseType.valueOf(type))
                .build();

        Optional<User> user = userService.searchUser(idUser);
        if(user.isPresent()){
            release.setUser(user.get());
        } else {
            return new ResponseEntity("User not found.", HttpStatus.BAD_REQUEST);
        }

        List<Release> releaseList = releaseService.search(release);
        return new ResponseEntity(releaseList, HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity updateStatus(@PathVariable("id") Long id, @RequestBody ReleaseStatusDTO releaseStatusDTO){

        return releaseService.searchReleaseById(id).map( result -> {
            try{
                ReleaseStatus releaseStatus = ReleaseStatus.getReleaseStatus(releaseStatusDTO.getStatus());
                if(Objects.isNull(releaseStatus)){
                    return new ResponseEntity("Status invalid.", HttpStatus.BAD_REQUEST);
                }
                result.setReleaseStatus(releaseStatus);
                releaseService.update(result);
                return new ResponseEntity(result, HttpStatus.OK);
            } catch (BusinessRuleException e){
                return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }).orElseGet(() -> new ResponseEntity("Release not found", HttpStatus.BAD_REQUEST));

    }

    private Release convert(ReleaseDTO releaseDTO){

        User user = userService
                .searchUser(releaseDTO.getUserId())
                .orElseThrow(() -> new BusinessRuleException("User not found."));

        return Release.builder()
                .id(releaseDTO.getId())
                .description(releaseDTO.getDescription())
                .month(releaseDTO.getMonth())
                .year(releaseDTO.getYear())
                .value(releaseDTO.getValue())
                .user(user)
                .releaseType(ReleaseType.valueOf(releaseDTO.getType()))
                .releaseStatus(releaseDTO.getStatus() != null ? ReleaseStatus.getReleaseStatus(releaseDTO.getStatus()) : null)
                .build();
    }
}
