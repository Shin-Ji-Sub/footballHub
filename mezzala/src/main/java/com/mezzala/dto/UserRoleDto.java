package com.mezzala.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserRoleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;
    private String roleKorName;

}
