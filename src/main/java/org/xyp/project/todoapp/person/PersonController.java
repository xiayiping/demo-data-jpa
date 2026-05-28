package org.xyp.project.todoapp.person;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xyp.project.todoapp.infra.txop.TransactionalOp;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("api/person")
public class PersonController {

    final TransactionalOp txOp;
    final PersonRepo personRepo;

    @GetMapping
    public Person getPerson() {
        return txOp.returnInTx(() -> {
            final var p = new Person();
            final var t = System.currentTimeMillis();
            p.setId(new PersonId(t));

            final var a = new Address();
            a.setId(new AddressId(t));
            p.setUsername("p-" + t);
            p.addAddress(a);

            return personRepo.save(p);
        });
    }

    @GetMapping("{id}")
    public Person getPersonById(@PathVariable Long id) {
        return txOp.returnInTx(() -> Objects.requireNonNull(personRepo.findById(new PersonId(id)).orElse(null)));
    }

    @PutMapping("{id}")
    public Person updatePersonById(@RequestBody Person person, @PathVariable Long id) {
        return txOp.returnInTx(() -> {
            final var p = personRepo.findById(new PersonId(id));
            return Objects.requireNonNull(p.map(e -> {
                e.setUsername(person.getUsername());
                System.out.println(e.getAddresses().getClass());
                return e;
            }).orElse(null));
        });
    }
}
