package com.farmconnect.krishisetu.security.facade;

import java.util.Optional;

public interface UserSecurityFacade {

    Optional<SecurityUserDto> findByEmail(String email);

}
