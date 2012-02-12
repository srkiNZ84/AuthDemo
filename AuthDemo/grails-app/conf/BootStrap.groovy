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
		
		//def samFirstPost = Post.findByTitle("Sam's first Post") ?: new Post(title: "Sam's first Post", content: "The first post by Sam.", user: samUser).save()
		//def samSecondPost = Post.findByTitle("Sam's second Post") ?: new Post(title: "Sam's second Post", content: "The second post by Sam.", user: samUser).save()
		
		def joePost = Post.findByTitle("Joe's post") ?: new Post(title: "Joe's post", content: "Joes post.", user: joeUser).save()
		
		def bobPost = Post.findByTitle("Bob's post") ?: new Post(title: "Bob's post", content: "Bobs post.", user: bobUser).save()
    }
    def destroy = {
    }
}
