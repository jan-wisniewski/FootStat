package com.janwisniewski.domain.elems;

import com.janwisniewski.domain.elems.types.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class Event {
    protected String surname;
    protected Integer min;
    protected EventType eventType;

}
