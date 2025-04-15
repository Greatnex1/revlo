package com.nouah.revlo.service.interfaces;

import com.nouah.revlo.dto.request.AuthenticationRequest;
import com.nouah.revlo.dto.response.AuthenticationResponse;

public interface AuthUseCase {
    AuthenticationResponse authenticate(AuthenticationRequest request);

}
