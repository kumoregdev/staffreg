package org.kumoricon.staff.badgelib.badgeimage;


import org.kumoricon.staff.badgelib.StaffBadgeDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BadgeCreatorStaff2018Back implements BadgeCreator {
    private static final int DPI = 300;
    private static final int BADGE_WIDTH = (int) 4.25*DPI;      // 4x6 inch badge with overprint
    private static final int BADGE_HEIGHT = (int) 6.25*DPI;
    private Font font;

    @Override
    public byte[] createBadge(StaffBadgeDTO attendee) {
        if (font == null) font = new Font("Dialog", Font.BOLD, 36);
        BadgeImage b = new BadgeImage(BADGE_WIDTH, BADGE_HEIGHT, DPI);

        drawBadgeTypeStripe(b, attendee);
        drawAgeColorStripe(b, attendee);
        drawName(b, attendee);
        drawBadgeImage(b, attendee);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(b.getImage(), "png", baos);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] createBadge(StaffBadgeDTO attendee, Font font) {
        this.font = font;
        return createBadge(attendee);
    }

    private void drawBadgeImage(BadgeImage b, StaffBadgeDTO attendee) {
        Rectangle badgeImageLocation = new Rectangle(280, 310, 660, 660);
//        b.fillRect(badgeImageLocation, Color.LIGHT_GRAY);
        b.drawStretchedImage(attendee.getBadgeImage(), badgeImageLocation);
    }


    @SuppressWarnings("Duplicates")
    private void drawAgeColorStripe(BadgeImage b, StaffBadgeDTO attendee) {
        Rectangle ageBackground = new Rectangle(0, 1560, BADGE_WIDTH, 225);
        Color bgColor = Color.decode(attendee.getAgeBackgroundColor());
        Color fgColor = BadgeImage.getInverseColor(bgColor);
        b.fillRect(ageBackground, bgColor);
        b.drawStretchedCenteredString("STAFF", ageBackground, font, fgColor);
    }

    private void drawName(BadgeImage b, StaffBadgeDTO attendee) {
        Rectangle nameBg = new Rectangle(300, 1250, 650, 300);

        String[] names = {attendee.getFirstName(), attendee.getLastName()};
        b.drawCenteredStrings(names, nameBg, font, Color.BLACK);
    }

    private void drawBadgeTypeStripe(BadgeImage b, StaffBadgeDTO attendee) {
        if (attendee != null) {
            Color bgColor = Color.decode(attendee.getDepartmentBackgroundColor());
            Color fgColor = BadgeImage.getInverseColor(bgColor);
            Rectangle positionsBackground = new Rectangle(976, 0, 225, 1575);
            b.fillRect(positionsBackground, bgColor);

            Rectangle textBounds = new Rectangle(990, 50, 150, 1475);
            b.drawRotatedCenteredStrings(attendee.getPositions(), textBounds, font, fgColor, false);
        }
    }
}
