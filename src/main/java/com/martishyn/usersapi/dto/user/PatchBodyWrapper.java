package com.martishyn.usersapi.dto.user;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PatchBodyWrapper {
    @JsonProperty(value = "data")
    private Map<String, Object> patchBody = new HashMap<>();

    @JsonAnySetter
    public void addPatchBody(String type, Object object) {
        patchBody.put(type, object);
    }

    @JsonAnyGetter
    public Map<String, Object> getPatchBody() {
        return patchBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatchBodyWrapper that = (PatchBodyWrapper) o;
        return Objects.equals(patchBody, that.patchBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patchBody);
    }
}
