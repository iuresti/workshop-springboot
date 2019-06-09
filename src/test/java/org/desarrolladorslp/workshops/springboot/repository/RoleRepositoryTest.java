package org.desarrolladorslp.workshops.springboot.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.desarrolladorslp.workshops.springboot.models.Role;
import org.desarrolladorslp.workshops.springboot.models.RoleName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;

    @Before
    public void setup() {
        entityManager.clear();
    }

    @Test
    public void givenAdminRoleInStorageAndValidRoleName_whenFindRoleAdminByName_thenRetrieveRole() {
        // given
        Role adminRole = new Role().builder()
                .name(RoleName.ROLE_ADMIN).description("Role for Admin User")
                .build();
        adminRole = entityManager.persist(adminRole);

        // when
        Optional<Role> repoAdminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);

        // then
        assertTrue(repoAdminRole.isPresent());
        assertTrue(adminRole.getId().equals(repoAdminRole.get().getId()));
        assertTrue(adminRole.getName().equals(repoAdminRole.get().getName()));

    }

    @Test
    public void givenAdminRoleInStorage_whenFindRoleUserByName_thenRetrieveEmpty() {
        // given
        Role adminRole = new Role().builder()
                .name(RoleName.ROLE_ADMIN).description("Role for Admin User")
                .build();
        adminRole = entityManager.persist(adminRole);

        // When
        Optional<Role> repoUserRole = roleRepository.findByName(RoleName.ROLE_USER);

        // then
        assertFalse(repoUserRole.isPresent());

    }

}
