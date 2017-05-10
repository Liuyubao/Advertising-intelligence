package checkit.facialrecognition.ias.camera;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Camera {

	private final VideoCapture capture;
	// number of the videodevice, usually 0
	private final int videodevice;

	public Camera(int videodevice) throws CameraException {
		this.videodevice = videodevice;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.capture = new VideoCapture();
		this.startCamera();

	}

	private void startCamera() throws CameraException {
		this.capture.open(this.videodevice);
		if (this.capture.isOpened()) {
			System.out.println("Video capture opened");
		} else {
			System.out.println("The camera can't be opened...");
			throw new CameraException();
		}
	}

	public BufferedImage takePicture() throws CameraException {
		Mat frame = new Mat();
		if (this.capture.isOpened()) {
			this.capture.read(frame);
			return this.MatToImage(frame);

		} else {
			System.out.println("The camera can't be opened...");
			throw new CameraException();
		}
	}

	private BufferedImage MatToImage(Mat frame) {
		int width = frame.width(), height = frame.height(), channels = frame.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		frame.get(0, 0, sourcePixels);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		return image;

	}
}
