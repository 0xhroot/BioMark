package com.example.biomark;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FaceRecognitionHelper {

    private Interpreter interpreter;

    public FaceRecognitionHelper(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, "mobile_face_net.tflite"));
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFileName) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFileName);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /**
     * Computes the face embedding for a given bitmap.
     * Assumes the model input size is 112x112 and that the pixel values are normalized between -1 and 1.
     */
    public float[] getFaceEmbedding(Bitmap bitmap) {
        // Resize the bitmap to 112x112
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 112, 112, true);
        float[][][][] input = new float[1][112][112][3];

        for (int i = 0; i < 112; i++) {
            for (int j = 0; j < 112; j++) {
                int pixel = resized.getPixel(j, i);
                // Extract RGB channels, normalize to [-1, 1]
                input[0][i][j][0] = ((pixel >> 16) & 0xFF) / 128.0f - 1.0f;
                input[0][i][j][1] = ((pixel >> 8) & 0xFF) / 128.0f - 1.0f;
                input[0][i][j][2] = (pixel & 0xFF) / 128.0f - 1.0f;
            }
        }

        // Prepare output array; assuming output embedding dimension is 128.
        float[][] output = new float[1][128];
        interpreter.run(input, output);
        return output[0];
    }
}
