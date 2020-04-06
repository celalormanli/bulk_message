package com.qijik.bulkmessage

class Model {
    var isSelected:Boolean=false
    var person:String?=null
    fun getPersons():String{
        return  this!!.person.toString()
    }
    fun  setPersons(person: String){
        this.person=person
    }
    fun getSelecteds():Boolean{
        return isSelected
    }
    fun setSelecteds(selected:Boolean){
        this.isSelected=selected
    }
}