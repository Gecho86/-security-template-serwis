package com.j24.security.template.service;

import com.j24.security.template.model.Account;
import com.j24.security.template.model.AccountRole;
import com.j24.security.template.model.dto.UserRegistrationRequest;
import com.j24.security.template.repository.AccountRepository;
import com.j24.security.template.repository.AccountRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${default.user.roles:USER}")
    private String[] defaultUserRegisterRoles;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void removeAccount(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (!account.isAdmin()) {
                accountRepository.delete(account);
            }
        }
    }

    public void toggleLock(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            account.setLocked(!account.isLocked());

            accountRepository.save(account);
        }
    }

    public boolean register(UserRegistrationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            return false;
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        account.setRoles(findRolesByName(defaultUserRegisterRoles));

        accountRepository.save(account);

        return true;
    }

    public Set<AccountRole> findRolesByName(String... roles) {
        Set<AccountRole> accountRoles = new HashSet<>();
        for (String role : roles) {
            accountRoleRepository.findByName(role).ifPresent(accountRoles::add);
        }
        return accountRoles;
    }

    public Optional<Account> getById(Long id) {
        return accountRepository.findById(id);
    }

    public void updateRoles(Long accountId, HttpServletRequest request) {
        // klucz w mapie to nazwa parametru
        // wartość to tablica, gdzie 0 element to wartość pola
        // accountId -> String[] {"2", "2"}
        // ADMIN -> String[] {"on"}
        // USER -> String[] {"on"} (jeśli nie będzie zaznaczony to nie wystąpi w mapie)

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            Map<String, String[]> parameterMap = request.getParameterMap();
            Set<AccountRole> accountRoles = new HashSet<>();

            for (String key : parameterMap.keySet()) {
                accountRoleRepository.findByName(key).ifPresent(accountRoles::add);
            }

            account.setRoles(accountRoles);
            accountRepository.save(account);
        }
    }
}
