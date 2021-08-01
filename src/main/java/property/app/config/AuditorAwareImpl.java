package property.app.config;

import property.app.beans.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
            return Optional.of("master@gmail.com");
        }
        return Optional.of(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
    }
}
