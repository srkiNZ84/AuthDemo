package org.authdemo

import java.util.Date;

class Post {
	
	Date dateCreated
	Date lastUpdated
	
	String title
	String content
	
	static belongsTo = [user:User]
	static hasMany = [comments:Comment]

    static constraints = {
		title(unique: true, blank: false)
		content(maxSize:1000)
    }
	
	String toString(){
		return title
	}
}
