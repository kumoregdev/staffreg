package org.kumoricon.staffserver.user;

import org.kumoricon.staffserver.staff.Staff;
import org.kumoricon.staffserver.staff.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() > 0 || privilegeRepository.count() > 0 || roleRepository.count() > 0) {
            log.info("Found data in user/role/privillege tables, skipping setup");
            return;
        }

        log.info("No data found in user/role/privilege tables, setting up default data");
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Collections.singletonList(adminRole));
        user.setEnabled(true);
        user.setPasswordResetRequired(true);
        log.info("admin password hashed: " + user.getPassword());
        userRepository.save(user);
        log.info("Created default user admin / password");
    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    protected Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}