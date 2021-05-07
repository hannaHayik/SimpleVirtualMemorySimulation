

/**
 * 
 * @author Name: Hanna Hayek            ID: 207442054
 * 
 */
public class Page {  // this data structure gives us more flexibility than Nodes/Links.
	Page element;    // field for the element it self.
	Page next;       // pointers next/previous of Pages.
	Page prev;
    String data;     // the string that the Page contains.
    int key;         // a unique key for every Page according to the secondaryMemory.
    
    public Page(String data, int key) {  // a constructor for initializing the Pages at first.
		this.data=data;  
		this.key=key;
	}
    public Page(Page element, Page next, Page prev) {    // a constructor for the doublyLL to set the pointers of every Page.
        this.element = element;
        this.next = next;
        this.prev = prev;
    }
         // regular Get's and Set's function for every field.
    public String getData() {
		return this.data;
	}
	
	public int getKey() {
		return this.key;
	}
	
	public Page getNext() {
		return this.next;
	}
	
	public Page getPrev() {
		return this.prev;
	}
	
	public void setNext(Page next) {
		this.next=next;
	}
	
	public void setPrev(Page prev) {
		this.prev=prev;
	}
	public void setData(String data) {
		this.data=data;
	}
	
	public void setKey(int key) {
		this.key=key;
	}
}
