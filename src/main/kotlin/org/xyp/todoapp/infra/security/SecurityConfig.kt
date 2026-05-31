package org.xyp.todoapp.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import org.xyp.todoapp.core.auth.UserDetailsInfo
import org.xyp.todoapp.core.txop.TransactionalOp
import org.xyp.todoapp.domain.user.UserService


@Configuration
class SecurityConfig {

    @Bean
    fun userDetailsService(userSvc: UserService, txOp: TransactionalOp): UserDetailsService {
        return UserDetailsService { username ->
            txOp.returnInNewTx {
                userSvc.findByUsername(username)?.let {
                    UserDetailsInfo(
                        it.id,
                        it.username ?: "",
                        it.password ?: "",
                        listOf()
                    )
                }
            } ?: throw UsernameNotFoundException("User not found $username")
        }
    }


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http.authorizeHttpRequests { authRequest ->
            authRequest
                .requestMatchers("/ui").authenticated()
                .requestMatchers("/ui/").authenticated()
                .requestMatchers("/ui/**").authenticated()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
        }
        http.formLogin(Customizer.withDefaults())
        http.csrf { csrf -> csrf.disable() }
        http.cors { cors -> cors.disable() }
        http.headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
        return http.build()
    }
}