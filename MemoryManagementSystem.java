/**
 * 
 * @author Name: Hanna Hayek
 * 
 */
import java.util.Arrays;
import java.util.NoSuchElementException;


public class MemoryManagementSystem{
	public String[] secondaryMemory;
	private boolean useLRU;
	private DoublyLinkedListImpl<Page> ram;   // RAM using a doublyLL with objects of Page class instead of Link/Nodes.
	private boolean[] active;                // boolean array to tell if the Page is already in the RAM or not.
	private Page[] indicator;                 // an array of Pages to have access within O(1) to every Page in RAM/secondaryMemory.
	 
	 
	public MemoryManagementSystem(int mainMemorySize, int secondaryMemorySize, boolean useLRU) {
		secondaryMemory=new String[secondaryMemorySize];  // regular array initialize.
		for(int i=0; i<secondaryMemorySize; i++) 
			secondaryMemory[i]="";                       // secondaryMemory given empty strings as asked before.
		this.useLRU=useLRU;
		this.ram=new DoublyLinkedListImpl<Page>();
		this.active=new boolean[secondaryMemorySize];         // regular initialize here too...
		this.indicator=new Page[secondaryMemorySize];
		for(int i=0; i<secondaryMemorySize; i++)
			this.active[i]=false;                        // didn't trust the java default values :D, so we did it manually.
		for(int i=0; i<secondaryMemorySize; i++) {
			Page newpage=new Page(this.secondaryMemory[i], i);
		    this.indicator[i]=newpage;                   // creating an object (Page) for every string in the secondaryMemory and inserting it into  
			                                            // indicator array of Pages.
		}
		for(int i=0; i<mainMemorySize; i++) {
			this.ram.addFirst(indicator[i]);           // adding the first n Pages to the RAM as asked in the homework.
			active[i]=true;                            // changing the active array values for the entered pages to indicate that they 
		}                                              // currently are being used in the RAM.
		}
		
	

	@Override
	public String toString() {
		return "secondaryMemory=" + Arrays.toString(secondaryMemory);
	}
	
	public String read(int index) {
        if(useLRU==true) {                  // the function has two options : using LRU strategy or not, second option will go to FIFO strategy.
		if(this.active[index]==true) {      // 2 options here two, either the Page we want is in the RAM or it's not.
            if(this.indicator[index]==this.ram.head)  // if it's then simply go to the indicator array and return the data field of the Page.
            	return this.indicator[index].getData();
            if(this.indicator[index]==this.ram.tail) {   // two checks to see if it's in the RAM head/tail so we avoid the 
            	this.ram.addFirst(this.ram.removeLast());  // null pointer exception.
            	return this.ram.head.getData();
            }
            	
			this.indicator[index].getPrev().setNext(this.indicator[index].getNext());  // if it's in the middle and we are using LRU then we change 
			this.indicator[index].getNext().setPrev(this.indicator[index].getPrev());  // it's pointers (deleting it). changing the indicator array
			this.ram.addFirst(this.indicator[index]);      // means we are changing the doublyLL (RAM) because they have the same objects.
			return this.indicator[index].getData();    // because we are using LRU we add the Page back into the first of the LL and return the string.
		     }
		this.active[this.ram.tail.getKey()]=false;  // wanted Page not in RAM, we take out the RAM tail according to LRU strategy and change the value
		int x=this.ram.tail.getKey();               // to false. After that we save the index and string of it to update it in the secondaryMemory array.
		String str=this.ram.tail.getData();
		this.ram.removeLast();                    
		this.ram.addFirst(this.indicator[index]);  // we add the wanted Page to the RAM head because it's the most recently used.
		this.secondaryMemory[x]=str;              // updating the Page we took out.
    	this.active[index]=true;                   // updating the boolean value for the wanted page to indicate it's existence in the RAM. 
    	return this.indicator[index].getData();
        }
        else {
        	if(this.active[index]==true)            // simple in the FIFO option, if the Page is in the RAM return the string of it.
        		return this.indicator[index].getData();
        	this.active[this.ram.tail.getKey()]=false;   // if not we do as we did before, delete the RAM tail and update pointers and boolean values
        	int x=this.ram.tail.getKey();               // and add the new page to the RAM.
    		String str=this.ram.tail.getData();
        	this.ram.removeLast();
        	this.ram.addFirst(this.indicator[index]);
        	this.secondaryMemory[x]=str;
        	this.active[index]=true;
        	return this.indicator[index].getData();
        }
	}

	public void write(int index, char c) {
		if(useLRU==true) {
			if(this.active[index]==true) {          // as the function read, almost the same but instead here we update the string in the RAM only
				if(this.indicator[index]==this.ram.head) {     // if the Page was deleted ( replaced by another ) we update it in the secondaryMemory.
					this.ram.head.setData(this.ram.head.getData()+c);
					return;            // empty returns statements so the program won't continue.
				}
				if(this.indicator[index]==this.ram.tail) {
					this.ram.tail.setData(this.ram.tail.getData()+c);
					Page tmp=this.ram.tail;
					this.ram.removeLast();
					this.ram.addFirst(tmp);
					return;
				}
				this.indicator[index].getPrev().setNext(this.indicator[index].getNext());
				this.indicator[index].getNext().setPrev(this.indicator[index].getPrev());     // same as above.
				this.ram.addFirst(this.indicator[index]);
				this.ram.head.setData(this.ram.head.getData()+c);
				return;
			}
			else {
			this.active[this.ram.tail.getKey()]=false;
			int x=this.ram.tail.getKey();
    		String str=this.ram.tail.getData();             // same as above.
			this.ram.removeLast();
			this.ram.addFirst(this.indicator[index]);
			this.secondaryMemory[x]=str;
	    	this.active[index]=true;
	    	this.ram.head.setData(this.ram.head.getData()+c);
			return;
			}
		}
		else {
			if(this.active[index]==true)
				this.indicator[index].setData(this.indicator[index].getData()+c);
			else {
				this.active[this.ram.tail.getKey()]=false;
				int x=this.ram.tail.getKey();          // same as above.
	    		String str=this.ram.tail.getData();
				this.ram.removeLast();
				this.ram.addFirst(this.indicator[index]);
				this.secondaryMemory[x]=str;
		    	this.active[index]=true;
		    	this.ram.head.setData(this.ram.head.getData()+c);
				return;
				
			}
		}
	}
	
	
	public class DoublyLinkedListImpl<E> {
		 
	    private Page head;         // using doublyLL with Pages instead of Links/Nodes is more flexible, we got an array of Pages too 
	    private Page tail;         // so we can access the wanted Page in RAM in O(1) time complexity.
	    private int size;
	     
	    public DoublyLinkedListImpl() {
	        size = 0;
	    }

	    public int size() { 
	    	return size;
	      }

	    public boolean isEmpty() { 
	    	return size == 0; 
	    	}

	    public void addFirst(Page element) {      // adds an object to the Head(start) of the RAM.
	    	element.setNext(head);
	    	element.setPrev(null);
	        if(head != null ) {head.prev = element;}     
	        head = element;
	        if(tail == null) { tail = element;}
	        size++;
	    }

	    public void addLast(Page element) {        
	    	Page tmp = new Page(element, null, tail);      // adds an object to the tail(end) of the RAM.
	        if(tail != null) {tail.next = tmp;}
	        tail = tmp;
	        if(head == null) { head = tmp;}
	        size++;

	    }
	      
	    public Page removeFirst() {       // removes the first element in the RAM(doublyLL).
	        if (size == 0) throw new NoSuchElementException();
	        Page tmp = head;
	        head = head.next; 
	        head.prev = null;
	        size--;
	        return tmp.element;
	    }
	      
	    public Page removeLast() {       // removes the last element in the RAM(doublyLL).
	        if (size == 0) throw new NoSuchElementException();
	        Page tmp = tail;
	        tail = tail.prev;
	        tail.next = null;
	        size--;
	        return tmp.element;
	    }
}
}
