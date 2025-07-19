package com.qtechgames.rollernetwork.service.impl;

import com.qtechgames.rollernetwork.service.IRollerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RollerService implements IRollerService {

    public RollerService() {
    }

    @Override
    public String getFullName(final String lastname) {
        return "Raúl " + lastname;
    }

}
