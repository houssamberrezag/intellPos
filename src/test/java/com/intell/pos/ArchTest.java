package com.intell.pos;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.intell.pos");

        noClasses()
            .that()
            .resideInAnyPackage("com.intell.pos.service..")
            .or()
            .resideInAnyPackage("com.intell.pos.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.intell.pos.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
