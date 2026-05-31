package org.xyp.todoapp.app.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.xyp.todoapp.core.enums.ActiveStatus;
import org.xyp.todoapp.core.optfield.OptField;
import org.xyp.todoapp.domain.user.UserId;

import java.time.LocalDateTime;

public class UserDto {
    UserId id;
    String username;
    String password;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    ActiveStatus activeStatus;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    OptField<UserDto> friend;

    public UserDto(UserId id, String username, String password) {
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

    public String getPassword() {
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

    public OptField<UserDto> getFriend() {
        return friend;
    }

    public void setFriend(OptField<UserDto> friend) {
        this.friend = friend;
    }
}
