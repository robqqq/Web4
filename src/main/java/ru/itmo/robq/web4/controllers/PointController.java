package ru.itmo.robq.web4.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.itmo.robq.web4.checker.PointChecker;
import ru.itmo.robq.web4.data.PointRepository;
import ru.itmo.robq.web4.data.UserRepository;
import ru.itmo.robq.web4.exceptions.ValidationException;
import ru.itmo.robq.web4.model.Point;
import ru.itmo.robq.web4.model.User;
import ru.itmo.robq.web4.security.userDetails.CustomUserDetails;
import ru.itmo.robq.web4.validators.PointValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
@Getter
@Setter
@NoArgsConstructor
public class PointController {
    private PointRepository pointRepo;
    private UserRepository userRepo;
    private PointValidator validator;
    private PointChecker checker;

    @Autowired
    public PointController (PointRepository pointRepo,
                            UserRepository userRepo,
                            PointValidator validator,
                            PointChecker checker) {
        this.pointRepo = pointRepo;
        this.userRepo = userRepo;
        this.validator = validator;
        this.checker = checker;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(
            @RequestParam("x") Double x,
            @RequestParam("y") Double y,
            @RequestParam("r") Double r) {
        Point point = new Point(x, y, r);
        try {
            Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            User user = userRepo.getById(userId);
            point.setUser(user);
            validator.validate(point);
            point.setResult(checker.check(point));
            return ResponseEntity.ok().body(pointRepo.save(point));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/clear")
    public ResponseEntity<?> clear() {
        try {
            Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            return ResponseEntity.ok().body(pointRepo.deleteAllByUserUid(userId));
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        try {
            Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            return ResponseEntity.ok().body(pointRepo.findAllByUserUid(userId));
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
