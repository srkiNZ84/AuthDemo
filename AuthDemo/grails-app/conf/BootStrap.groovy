import org.authdemo.*

class BootStrap {

    def init = { servletContext ->
		def userRole = Role.findByAuthority("ROLE_USER") ?: new Role(authority: "ROLE_USER").save()
		def posterRole = Role.findByAuthority("ROLE_POSTER") ?: new Role(authority: "ROLE_POSTER").save()
		def adminRole = Role.findByAuthority("ROLE_ADMIN") ?: new Role(authority: "ROLE_ADMIN").save()
		
		// Sam has the User role
		def samUser = User.findByUsername("sam") ?: new User(username: "sam", password: "123sam!", enabled: true).save()
		if(!samUser.authorities.contains(userRole)){
			UserRole.create(samUser, userRole)
		}
		
		// Joe has the Poster role
		def joeUser = User.findByUsername("joe") ?: new User(username: "joe", password: "123joe!", enabled: true).save()
		if(!joeUser.authorities.contains(posterRole)){
			UserRole.create(joeUser, posterRole)
		}
		
		// Bob has the Admin role
		def bobUser = User.findByUsername("bob") ?: new User(username: "bob", password: "123bob!", enabled: true).save()
		if(!bobUser.authorities.contains(adminRole)){
			UserRole.create(bobUser, adminRole)
		}
    }
    def destroy = {
    }
}
