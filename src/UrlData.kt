package splider

data class UrlData (val title: String, val url: String){
    fun getElementID() : String{
        return url.replace("/","_")
    }
}

