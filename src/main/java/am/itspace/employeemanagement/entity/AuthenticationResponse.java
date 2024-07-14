package am.itspace.employeemanagement.entity;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String jwt) {

    public AuthenticationResponse(String jwt){
        this.jwt=jwt;
    }
    public String getJwt() {
        return jwt;
    }

}