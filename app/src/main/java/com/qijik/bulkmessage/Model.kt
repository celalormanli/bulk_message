package com.qijik.bulkmessage

class Model {
    var isSelected:Boolean=false
    var person:String?=null
    var number:String?=null
    fun getPersons():String{
        return  this!!.person.toString()
    }
    fun  setPersons(person: String){
        this.person=person
    }
    fun getNumbers():String{
        return  this!!.number.toString()
    }
    fun  setNumbers(person: String){
        this.number=person
    }
    fun getSelecteds():Boolean{
        return isSelected
    }
    fun setSelecteds(selected:Boolean){
        this.isSelected=selected
    }
}