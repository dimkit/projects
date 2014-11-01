package shared.com;

/**Contains everything needed to store an image. Will eventually be used to transfer the actual image, not just its URL.
 * 
 * @author Kristopher Miles
 *
 */
public class ImageWrapper {
	private String pictureURL;

	public ImageWrapper(String picture) {
		super();
		this.pictureURL = picture;
	}

	public String getPicture() {
		return pictureURL;
	}

	public void setPicture(String picture) {
		this.pictureURL = picture;
	}
	

}
