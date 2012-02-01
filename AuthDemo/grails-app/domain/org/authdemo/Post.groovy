package org.authdemo

class Post {
	
	String title
	String content
	
	static belongsTo = [user:User]

    static constraints = {
		title()
		content(maxSize:1000)
    }
	
	String toString(){
		return title
	}
}
