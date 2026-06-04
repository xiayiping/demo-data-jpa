package org.xyp.todoapp.app.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.access.prepost.PostAuthorize;
import org.xyp.todoapp.core.enums.ActiveStatus;
import org.xyp.todoapp.core.json.OptField;
import org.xyp.todoapp.domain.user.UserId;

import java.time.LocalDateTime;

public class UserDtoJava {
    UserId id;
    String username;
    String password;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    ActiveStatus activeStatus;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    OptField<UserDtoJava> friend;

    public UserDtoJava(UserId id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public UserId getId() {
        return id;
    }


    public void setId(UserId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @PostAuthorize("hasAuthority('ROLE_USEReeee')")
    public String getPassword() {
        System.out.println("-----------------------------------------------");
        System.out.println("-----------------------------------------------");
        System.out.println("-----------------------------------------------");
        System.out.println("-----------------------------------------------");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public OptField<UserDtoJava> getFriend() {
        return friend;
    }

    public void setFriend(OptField<UserDtoJava> friend) {
        this.friend = friend;
    }
}
