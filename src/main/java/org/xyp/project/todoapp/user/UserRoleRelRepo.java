package org.xyp.project.todoapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRelRepo extends JpaRepository<UserRoleRel, UserRoleRelId> {
}
