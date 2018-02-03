package eu.andreschepers.authservice.springstartup;

import eu.andreschepers.authservice.data.Permission;
import eu.andreschepers.authservice.data.repositories.PermissionRepository;
import eu.andreschepers.authservice.rest.annotation.RestResourcePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestResourcePermissionLoading implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<RestResourcePermission> permissions = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        for(BeanDefinition bean : provider.findCandidateComponents("eu.andreschepers.authservice.rest.resource")) {
            Class<?> clazz;
            try {
                clazz = Class.forName(bean.getBeanClassName());
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RestResourcePermission.class)) {
                        RestResourcePermission permission = method.getAnnotation(RestResourcePermission.class);
                        permissions.add(permission);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        processPermissions(permissions);
    }

    private void processPermissions(List<RestResourcePermission> permissions) {
        List<Permission> dbPermissions = permissionRepository.findAll();
        removeObsolete(dbPermissions, permissions);
        addNew(dbPermissions, permissions);
    }

    private void removeObsolete(List<Permission> dbPermissions, List<RestResourcePermission> permissions) {
        Set<String> permissionNames = permissions.stream()
                .map(permission -> permission.name())
                .collect(Collectors.toSet());
        List<Permission> obsoletePermissions = dbPermissions.stream()
                .filter(permission -> !permissionNames.contains(permission.getName()))
                .collect(Collectors.toList());
        permissionRepository.delete(obsoletePermissions);
    }

    private void addNew(List<Permission> dbPermissions, List<RestResourcePermission> permissions) {
        Set<String> permissionNames = dbPermissions.stream()
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        Set<RestResourcePermission> restPermissions = permissions.stream()
                .filter(permission -> !permissionNames.contains(permission.name()))
                .collect(Collectors.toSet());
        for (RestResourcePermission restPermission : restPermissions) {
            if (permissionRepository.findByName(restPermission.name()) == null) {
                Permission dbPermission = new Permission();
                dbPermission.setName(restPermission.name());
                dbPermission.setDescription(restPermission.description());
                permissionRepository.save(dbPermission);
            }
        }
    }
}