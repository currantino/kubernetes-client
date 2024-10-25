/*
 * Copyright (C) 2015 Red Hat, Inc.
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
package io.fabric8.kubernetes.schema.generator.model;

import io.fabric8.kubernetes.schema.generator.ImportManager;
import io.fabric8.kubernetes.schema.generator.ImportOrderComparator;
import io.fabric8.kubernetes.schema.generator.schema.SchemaUtils;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Getter
public class ClassInformation implements ImportManager {

  private final Set<String> imports;
  private final String packageName;
  private final boolean isInterface;
  private final JsonSubTypes jsonSubTypes;

  ClassInformation(SchemaUtils schemaUtils, Map.Entry<String, Schema<?>> clazz) {
    imports = new TreeSet<>(new ImportOrderComparator());
    final var classKey = clazz.getKey();
    final var classSchema = clazz.getValue();
    packageName = schemaUtils.toModelPackage(classKey.substring(0, classKey.lastIndexOf('.')));
    isInterface = SchemaUtils.isInterface(classSchema);
    if (isInterface) {
      addImport("com.fasterxml.jackson.annotation.JsonSubTypes");
      addImport("com.fasterxml.jackson.annotation.JsonTypeInfo");
      addImport("com.fasterxml.jackson.databind.annotation.JsonTypeResolver");
      jsonSubTypes = new JsonSubTypes(classSchema);
    } else {
      jsonSubTypes = null;
    }
  }

  boolean isEditable() {
    return !isInterface();
  }
}
