package exceptions;

public class CarAlreadyExistException extends Exception {
	
	private static final long serialVersionUID = 1L;
	 
	 public CarAlreadyExistException()
	  {
	    super();
	  }
	 
	  /**This exception is triggered if the question already exists 
	  *@param s String of the exception
	  */
	  public CarAlreadyExistException(String s)
	  {
	    super(s);
	  }

}
