package com.project.smit.makemyplace.MakeMyPlace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Smit on 01-09-2017.
 */

public class TextCaptcha extends Captcha {

    protected TextOptions options;
    private int wordLength;
    private char mCh;

    public enum TextOptions {
        UPPERCASE_ONLY,
        LOWERCASE_ONLY,
        NUMBERS_ONLY,
        LETTERS_ONLY,
        NUMBERS_AND_LETTERS
    }

    public TextCaptcha(int wordLength, TextOptions opt) {
        new TextCaptcha(0, 0, wordLength, opt);
    }

    public TextCaptcha(int width, int height, int wordLength, TextOptions opt) {
        setHeight(height);
        setWidth(width);
        this.options = opt;
        usedColors = new ArrayList<>();
        this.wordLength = wordLength;
        this.image = image();
    }

    @Override
    protected Bitmap image() {
        Paint p = new Paint();
        p.setDither(true);
        p.setColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawRect(0, 0, getWidth(), getHeight(), p);
        Paint tp = new Paint();
        tp.setTextSize(getWidth() / getHeight() * 30);
        tp.setFakeBoldText(true);
        tp.setLinearText(true);

        Random r = new Random(System.currentTimeMillis());
        CharArrayWriter cab = new CharArrayWriter();
        this.answer = "";
        for (int i = 0; i < this.wordLength; i++) {
            char ch = ' ';
            switch (options) {
                case UPPERCASE_ONLY:
                    ch = (char) (r.nextInt(91 - 65) + (65));
                    break;
                case LOWERCASE_ONLY:
                    ch = (char) (r.nextInt(123 - 97) + (97));
                    break;
                case NUMBERS_ONLY:
                    ch = (char) (r.nextInt(58 - 49) + (49));
                    break;
                case LETTERS_ONLY:
                    ch = getLetters(r);
                    break;
                case NUMBERS_AND_LETTERS:
                    ch = getLettersNumbers(r);
                    break;
                default:
                    ch = getLettersNumbers(r);
                    break;
            }
            cab.append(ch);
            this.answer += ch;
        }

        char[] data = cab.toCharArray();
        for (int i = 0; i < data.length; i++) {
            this.x += (25 - (3 * this.wordLength)) + (Math.abs(r.nextInt()) % (50 - (1.2 * this.wordLength)));
            this.y = 50 + Math.abs(r.nextInt()) % 50;
            Canvas cc = new Canvas(bitmap);
            tp.setColor(color());
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            cc.drawText(data, i, 1, this.x, this.y, tp);
            tp.setTextSkewX(0);
        }
        return bitmap;
    }

    private char getLettersNumbers(Random r) {
        int rint = (r.nextInt(123 - 49) + (49));

        if (((rint > 90) && (rint < 97)))
            getLettersNumbers(r);
        else if (((rint > 57) && (rint < 65)))
            getLettersNumbers(r);
        else
            mCh = (char) rint;
        return mCh;
    }

    private char getLetters(Random r) {
        int rint = (r.nextInt(123 - 65) + (65));
        if (((rint > 90) && (rint < 97)))
            getLetters(r);
        else
            mCh = (char) rint;
        return mCh;
    }
}