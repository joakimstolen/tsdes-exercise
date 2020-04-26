package no.kristiania.mock.exam.backend.services;

import no.kristiania.mock.exam.backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;

/**
 * As login system is same as in class, this java class is adaptation
 * https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/intro/exercise-solutions/quiz-game/part-11/backend/src/main/java/org/tsdes/intro/exercises/quizgame/backend/service/UserService.java
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createUser(String userName, String name, String lastName, String password, String email, String role){
        String hashedPassword = passwordEncoder.encode(password);

        //If there is user with given username or email in database we can not create it again, return false
        if((em.find(Users.class, userName) != null) || (em.find(Users.class, email) != null)){
            return false;
        }

        Users users = new Users();
        users.setUserID(userName);
        users.setName(name);
        users.setLastName(lastName);
        users.setHashedPassword(hashedPassword);
        users.setRoles(Collections.singleton(role));
        users.setEnabled(true);
        users.setEmail(email);

        em.persist(users);

        return true;
    }

    public Users findUserByUserName(String userName){
        Users users = em.find(Users.class,userName);
        if(users == null){
            throw new IllegalStateException("No user with given userName");
        }
        users.getBookedTrips().size();
        return users;
    }
}
