package com.janwisniewski.infrastructure;

import com.janwisniewski.domain.ports.TeamStoragePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamStorage implements TeamStoragePort {
    @Override
    public List<String> doSth() {
        return List.of("DUPA");
    }
}
