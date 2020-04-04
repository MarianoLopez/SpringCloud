package com.z.userservice.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import com.z.jwt.domain.UserRoles
import com.z.zcoreblocking.utils.JpaAuditor
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.persistence.GenerationType.SEQUENCE

@NamedEntityGraph(name = User.USER_ROLES_GRAPH, attributeNodes = [
    NamedAttributeNode("roles")
])
@Entity
@DynamicUpdate
data class User (
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy =  SEQUENCE, generator = "user_sequence")
    var id:Long? = null,

    @get:JsonProperty(access = WRITE_ONLY)
    var password:String = "",

    @Column(unique = true)
    val name:String = "",

    var state:Boolean = true,

    @Column(name = "role", nullable = false)
    @ElementCollection(targetClass = UserRoles::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @JoinTable(name = "UserRoles", joinColumns = [JoinColumn(name = "userId")])
    var roles:MutableSet<UserRoles> = mutableSetOf(UserRoles.USER)
): JpaAuditor() {
    companion object Graphs {
        const val USER_ROLES_GRAPH = "user-roles"
    }
}