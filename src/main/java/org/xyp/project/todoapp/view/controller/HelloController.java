package org.xyp.project.todoapp.view.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xyp.project.todoapp.core.idgen.repository.IdTableRepository;
import org.xyp.project.todoapp.core.txop.TransactionalOp;
import org.xyp.project.todoapp.user.RoleRepo;
import org.xyp.project.todoapp.user.UserRepo;
import org.xyp.project.todoapp.user.UserRoleRelRepo;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    final TransactionalOp txOp;
    final IdTableRepository idTableRepository;
    final UserRoleRelRepo userRoleRelRepo;
    final UserRepo userRepo;
    final RoleRepo roleRepo;

    @GetMapping
    public Map<String, Object> sayHello() {
        final Map<String, Object> map = new HashMap<>();
        txOp.runInTx(() -> {
            final var ids = idTableRepository.findAll();
            ids.forEach(i -> log.info("{}", i));
            final var rolesRel = userRoleRelRepo.findAll();
            rolesRel.forEach(i -> log.info("{}", i));
            final var roles = roleRepo.findAll();
            final var users = userRepo.findAll();
            users.forEach(i -> {
                log.info("{}", i);
                i.addRolesTrans(roles);
            });

            map.put("ids", ids);
            map.put("rolesRel", rolesRel);
            map.put("users", users);
            map.put("roles", roles);
        });
        if (System.currentTimeMillis() % 2 == 0) {
            throw new RuntimeException("even number error!");
        }
        return map;
    }
}
