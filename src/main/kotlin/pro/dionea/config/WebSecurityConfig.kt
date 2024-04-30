package pro.dionea.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
class WebSecurityConfig(val dataSource: DataSource) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                    req -> req.requestMatchers("/", "/index",
                "/webjars/**", "/spams/view", "/filter/view/**", "/filter/check/**").permitAll()
                .anyRequest().authenticated()
            }
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/login")
                    .permitAll()
            }
            .logout { logout: LogoutConfigurer<HttpSecurity?> -> logout.permitAll() }
        return http.build()
    }

    @Bean
    fun user(encoder: PasswordEncoder): UserDetailsManager {
        val admin = User.builder()
            .username("admin")
            .password(encoder.encode("admin"))
            .roles("USER", "ADMIN")
            .build()
        return InMemoryUserDetailsManager(admin)
    }
}