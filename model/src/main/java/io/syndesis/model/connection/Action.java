/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.model.connection;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.syndesis.model.Kind;
import io.syndesis.model.WithId;
import io.syndesis.model.WithName;

import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(builder = Action.Builder.class)
@JsonIgnoreProperties(value = "properties", allowGetters = true)
public interface Action extends WithId<Action>, WithName, Serializable {

    @Override
    default Kind getKind() {
        return Kind.Action;
    }

    String getConnectorId();

    String getDescription();

    String getCamelConnectorGAV();

    String getCamelConnectorPrefix();

    DataShape getInputDataShape();

    DataShape getOutputDataShape();

    @Override
    default Action withId(String id) {
        return new Builder().createFrom(this).id(id).build();
    }

    class Builder extends ImmutableAction.Builder {
    }

    ActionDefinition getDefinition();

    @Deprecated
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    default Map<String, ConfigurationProperty> getProperties() {
        return Optional.ofNullable(getDefinition())
            .map(definition -> definition.getPropertyDefinitionSteps().stream()
                .flatMap(step -> step.getProperties().entrySet().stream())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue)))
            .orElse(Collections.emptyMap());
    }
}
