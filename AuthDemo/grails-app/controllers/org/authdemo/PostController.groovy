package org.authdemo

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

class PostController {
	def springSecurityService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [postInstanceList: Post.list(params), postInstanceTotal: Post.count()]
    }

	@Secured(["ROLE_POSTER", "ROLE_ADMIN"])
    def create() {
        [postInstance: new Post(params)]
    }

	@Secured(["ROLE_POSTER", "ROLE_ADMIN"])
    def save() {	
		def postInstance = new Post(params)	
		def user = User.get(springSecurityService.principal.id)
		
		def adminRole = Role.findByAuthority("ROLE_ADMIN")
		// Only the admin user can create posts with different authors. Everyone else has to use their own login
		if(postInstance.user.id != user.id && !user.authorities.contains(adminRole)){
			flash.message = "You can't create a post purporting to be someone else!"
			render(view: "create", model: [postInstance: postInstance])
			return
		}
        
        if (!postInstance.save(flush: true)) {
            render(view: "create", model: [postInstance: postInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'post.label', default: 'Post'), postInstance.id])
        redirect(action: "show", id: postInstance.id)
    }

    def show() {
        def postInstance = Post.get(params.id)
        if (!postInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "list")
            return
        }

        [postInstance: postInstance]
    }

	@Secured(["ROLE_POSTER", "ROLE_ADMIN"])
    def edit() {
        def postInstance = Post.get(params.id)
		def user = User.get(springSecurityService.principal.id)
		def adminRole = Role.findByAuthority("ROLE_ADMIN")
		if (!postInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "list")
            return
        }
		
		if(postInstance.user.id != user.id && !user.authorities.contains(adminRole)){
			flash.message = "This isn't your post to edit!"
			render(view: "show", model: [postInstance: postInstance])
			return
		}

        [postInstance: postInstance]
    }

	@Secured(["ROLE_POSTER", "ROLE_ADMIN"])
    def update() {
        def postInstance = Post.get(params.id)
		def user = User.get(springSecurityService.principal.id)
		def adminRole = Role.findByAuthority("ROLE_ADMIN")
        if (!postInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "list")
            return
        }
		
		if(postInstance.user.id != user.id && !user.authorities.contains(adminRole)){
			flash.message = "You can't edit a post created by someone else!"
			render(view: "show", model: [postInstance: postInstance])
			return
		}

        if (params.version) {
            def version = params.version.toLong()
            if (postInstance.version > version) {
                postInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'post.label', default: 'Post')] as Object[],
                          "Another user has updated this Post while you were editing")
                render(view: "edit", model: [postInstance: postInstance])
                return
            }
        }

        postInstance.properties = params

        if (!postInstance.save(flush: true)) {
            render(view: "edit", model: [postInstance: postInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'post.label', default: 'Post'), postInstance.id])
        redirect(action: "show", id: postInstance.id)
    }

	@Secured(["ROLE_POSTER", "ROLE_ADMIN"])
    def delete() {
        def postInstance = Post.get(params.id)
		def user = User.get(springSecurityService.principal.id)
		def adminRole = Role.findByAuthority("ROLE_ADMIN")
        if (!postInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "list")
            return
        }
		
		if(postInstance.user.id != user.id && !user.authorities.contains(adminRole)){
			flash.message = "This isn't your post to delete!"
			render(view: "show", model: [postInstance: postInstance])
			return
		}

        try {
            postInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'post.label', default: 'Post'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
