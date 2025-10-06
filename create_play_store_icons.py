#!/usr/bin/env python3
"""
Script to create Google Play Store icons
Requires: pip install pillow
"""

from PIL import Image, ImageDraw, ImageFont
import os

def create_app_icon(size=(512, 512), output_path="play_store_icon_512.png"):
    """Create a 512x512 app icon"""
    # Create image with gradient background
    img = Image.new('RGB', size, color='#2196F3')
    draw = ImageDraw.Draw(img)

    # Add gradient effect
    for y in range(size[1]):
        gradient = int(33 + (150 * y / size[1]))
        color = (33, gradient, 243)
        draw.line([(0, y), (size[0], y)], fill=color)

    # Draw game elements (simple platformer representation)
    # Platform
    platform_color = '#4CAF50'
    draw.rectangle([100, 400, 412, 450], fill=platform_color)

    # Player (simple character)
    player_color = '#FFC107'
    # Body
    draw.ellipse([200, 320, 280, 400], fill=player_color)
    # Head
    draw.ellipse([220, 280, 260, 320], fill=player_color)

    # Add text
    try:
        font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 80)
    except:
        font = ImageFont.load_default()

    # Draw text with shadow
    text = "JUMPER"
    bbox = draw.textbbox((0, 0), text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    text_x = (size[0] - text_width) // 2
    text_y = 50

    # Shadow
    draw.text((text_x + 3, text_y + 3), text, font=font, fill='#000000')
    # Main text
    draw.text((text_x, text_y), text, font=font, fill='#FFFFFF')

    img.save(output_path)
    print(f"Created: {output_path}")

def create_feature_graphic(size=(1024, 500), output_path="play_store_feature_1024x500.png"):
    """Create a 1024x500 feature graphic"""
    # Create image with gradient background
    img = Image.new('RGB', size, color='#1976D2')
    draw = ImageDraw.Draw(img)

    # Add gradient effect
    for y in range(size[1]):
        gradient = int(25 + (100 * y / size[1]))
        color = (25, gradient, 210)
        draw.line([(0, y), (size[0], y)], fill=color)

    # Draw platforms
    platform_color = '#4CAF50'
    draw.rectangle([50, 380, 300, 420], fill=platform_color)
    draw.rectangle([400, 320, 650, 360], fill=platform_color)
    draw.rectangle([750, 380, 974, 420], fill=platform_color)

    # Draw player characters
    player_color = '#FFC107'
    # First player
    draw.ellipse([140, 310, 210, 380], fill=player_color)
    draw.ellipse([155, 280, 195, 320], fill=player_color)

    # Second player (jumping)
    draw.ellipse([490, 240, 560, 310], fill=player_color)
    draw.ellipse([505, 210, 545, 250], fill=player_color)

    # Add enemies (simple red circles)
    enemy_color = '#F44336'
    draw.ellipse([800, 320, 860, 380], fill=enemy_color)
    draw.ellipse([820, 300, 840, 320], fill='#FFFFFF')  # Eye

    # Add text
    try:
        font_large = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 120)
        font_small = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 40)
    except:
        font_large = ImageFont.load_default()
        font_small = ImageFont.load_default()

    # Main title with shadow
    title = "JUMPER"
    bbox = draw.textbbox((0, 0), title, font=font_large)
    title_width = bbox[2] - bbox[0]
    title_x = (size[0] - title_width) // 2
    title_y = 50

    # Shadow
    draw.text((title_x + 4, title_y + 4), title, font=font_large, fill='#000000')
    # Main text
    draw.text((title_x, title_y), title, font=font_large, fill='#FFFFFF')

    # Subtitle
    subtitle = "Jump, Dodge & Survive!"
    bbox = draw.textbbox((0, 0), subtitle, font=font_small)
    subtitle_width = bbox[2] - bbox[0]
    subtitle_x = (size[0] - subtitle_width) // 2
    subtitle_y = 200

    draw.text((subtitle_x + 2, subtitle_y + 2), subtitle, font=font_small, fill='#000000')
    draw.text((subtitle_x, subtitle_y), subtitle, font=font_small, fill='#FFFFFF')

    img.save(output_path)
    print(f"Created: {output_path}")

if __name__ == "__main__":
    # Create output directory if needed
    output_dir = "play_store_assets"
    os.makedirs(output_dir, exist_ok=True)

    # Create icons
    icon_path = os.path.join(output_dir, "play_store_icon_512.png")
    feature_path = os.path.join(output_dir, "play_store_feature_1024x500.png")

    create_app_icon(output_path=icon_path)
    create_feature_graphic(output_path=feature_path)

    print("\n✓ All Google Play Store assets created successfully!")
    print(f"  - 512x512 icon: {icon_path}")
    print(f"  - 1024x500 feature graphic: {feature_path}")
