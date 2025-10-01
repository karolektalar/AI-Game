#!/usr/bin/env python3
"""
Skin Generator for Jumper Game
Generates 50x50px PNG images for each player skin with colors matching the game
"""

from PIL import Image, ImageDraw
import os

# Output directory
OUTPUT_DIR = "android/src/main/res/drawable"

# Skin definitions matching your game's PlayerSkin enum
SKINS = {
    "skin_classic": {
        "primary": (51, 232, 102),      # 0.2f, 0.9f, 0.4f
        "outline": (26, 128, 51),       # 0.1f, 0.5f, 0.2f
        "highlight": (128, 255, 153),   # 0.5f, 1.0f, 0.6f
    },
    "skin_fire": {
        "primary": (255, 102, 0),       # 1.0f, 0.4f, 0.0f
        "outline": (153, 51, 0),        # 0.6f, 0.2f, 0.0f
        "highlight": (255, 204, 102),   # 1.0f, 0.8f, 0.4f
    },
    "skin_ice": {
        "primary": (102, 204, 255),     # 0.4f, 0.8f, 1.0f
        "outline": (51, 102, 153),      # 0.2f, 0.4f, 0.6f
        "highlight": (204, 242, 255),   # 0.8f, 0.95f, 1.0f
    },
    "skin_electric": {
        "primary": (255, 255, 102),     # 1.0f, 1.0f, 0.4f
        "outline": (153, 153, 0),       # 0.6f, 0.6f, 0.0f
        "highlight": (255, 255, 204),   # 1.0f, 1.0f, 0.8f
    },
    "skin_shadow": {
        "primary": (77, 0, 77),         # 0.3f, 0.0f, 0.3f
        "outline": (26, 0, 26),         # 0.1f, 0.0f, 0.1f
        "highlight": (128, 51, 128),    # 0.5f, 0.2f, 0.5f
    },
    "skin_rainbow": {
        "primary": (255, 102, 204),     # 1.0f, 0.4f, 0.8f (Pink center)
        "outline": (153, 51, 128),      # 0.6f, 0.2f, 0.5f
        "highlight": (255, 204, 242),   # 1.0f, 0.8f, 0.95f
    }
}

def create_simple_square_skin(name, colors, size=50):
    """
    Create a simple square skin with border and highlight
    Matches the ShapeRenderer style used in the game
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Main body (slightly inset from edges, matching game code: bounds +2, -4)
    main_rect = [2, 2, size-3, size-3]
    draw.rectangle(main_rect, fill=colors['primary'] + (255,))

    # Border/outline (2 pixels thick on each side)
    outline_color = colors['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)  # Top
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)  # Bottom
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)  # Left
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)  # Right

    # Highlight effect (top bar, matching game code)
    highlight_rect = [5, size-8, size-5, size-5]
    draw.rectangle(highlight_rect, fill=colors['highlight'] + (255,))

    return img

def create_rainbow_gradient_skin(size=50):
    """
    Special rainbow skin with gradient effect
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Create rainbow gradient horizontally
    rainbow_colors = [
        (255, 0, 0),      # Red
        (255, 127, 0),    # Orange
        (255, 255, 0),    # Yellow
        (0, 255, 0),      # Green
        (0, 0, 255),      # Blue
        (139, 0, 255),    # Violet
    ]

    stripe_width = (size - 4) / len(rainbow_colors)

    for i, color in enumerate(rainbow_colors):
        x1 = 2 + int(i * stripe_width)
        x2 = 2 + int((i + 1) * stripe_width)
        draw.rectangle([x1, 2, x2, size-3], fill=color + (255,))

    # Add outline
    outline_color = SKINS['skin_rainbow']['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)

    return img

def create_fire_animated_skin(size=50):
    """
    Fire skin with flame-like pattern
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    colors = SKINS['skin_fire']

    # Base
    draw.rectangle([2, 2, size-3, size-3], fill=colors['primary'] + (255,))

    # Flame effect (gradient from bottom to top)
    flame_orange = (255, 102, 0, 255)
    flame_yellow = (255, 204, 0, 255)
    flame_red = (255, 0, 0, 255)

    # Bottom third - red
    draw.rectangle([4, size-10, size-5, size-3], fill=flame_red)

    # Middle third - orange
    draw.rectangle([6, size-20, size-7, size-11], fill=flame_orange)

    # Top third - yellow highlights
    draw.rectangle([8, size-30, size-9, size-21], fill=flame_yellow)

    # Outline
    outline_color = colors['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)

    return img

def create_ice_crystal_skin(size=50):
    """
    Ice skin with crystalline pattern
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    colors = SKINS['skin_ice']

    # Base
    draw.rectangle([2, 2, size-3, size-3], fill=colors['primary'] + (255,))

    # Crystal shards (diagonal lines)
    crystal_light = colors['highlight'] + (255,)
    crystal_dark = colors['outline'] + (255,)

    # Diagonal pattern
    for i in range(5, size-5, 8):
        draw.line([i, 5, i+10, 15], fill=crystal_light, width=2)
        draw.line([i+4, 20, i+14, 30], fill=crystal_dark, width=1)
        draw.line([i+2, 35, i+12, 45], fill=crystal_light, width=2)

    # Outline
    outline_color = colors['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)

    return img

def create_electric_spark_skin(size=50):
    """
    Electric skin with lightning bolt pattern
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    colors = SKINS['skin_electric']

    # Base
    draw.rectangle([2, 2, size-3, size-3], fill=colors['primary'] + (255,))

    # Lightning bolts
    white = (255, 255, 255, 255)

    # Zigzag pattern
    points = [
        (10, 5), (15, 15), (12, 15), (17, 25),
        (14, 25), (19, 35), (16, 35), (21, 45)
    ]

    for i in range(0, len(points)-1, 2):
        if i+1 < len(points):
            draw.line([points[i], points[i+1]], fill=white, width=2)

    # Mirror on right side
    points_right = [
        (40, 5), (35, 15), (38, 15), (33, 25),
        (36, 25), (31, 35), (34, 35), (29, 45)
    ]

    for i in range(0, len(points_right)-1, 2):
        if i+1 < len(points_right):
            draw.line([points_right[i], points_right[i+1]], fill=white, width=2)

    # Outline
    outline_color = colors['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)

    return img

def create_shadow_wisp_skin(size=50):
    """
    Shadow skin with smoky effect
    """
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    colors = SKINS['skin_shadow']

    # Base - darker
    draw.rectangle([2, 2, size-3, size-3], fill=colors['primary'] + (255,))

    # Wispy overlays with transparency
    wisp_color = colors['outline'] + (180,)
    highlight_wisp = colors['highlight'] + (120,)

    # Circular wisps
    draw.ellipse([8, 8, 20, 20], fill=wisp_color)
    draw.ellipse([25, 15, 40, 30], fill=wisp_color)
    draw.ellipse([15, 30, 30, 42], fill=highlight_wisp)

    # Outline
    outline_color = colors['outline'] + (255,)
    draw.rectangle([0, 0, size-1, 1], fill=outline_color)
    draw.rectangle([0, size-2, size-1, size-1], fill=outline_color)
    draw.rectangle([0, 0, 1, size-1], fill=outline_color)
    draw.rectangle([size-2, 0, size-1, size-1], fill=outline_color)

    return img

def main():
    """Generate all skin images"""

    # Create output directory if it doesn't exist
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    print(f"Generating skins in: {OUTPUT_DIR}")
    print("=" * 60)

    # Generate simple skins
    for name, colors in SKINS.items():
        if name == "skin_rainbow":
            img = create_rainbow_gradient_skin()
        elif name == "skin_fire":
            img = create_fire_animated_skin()
        elif name == "skin_ice":
            img = create_ice_crystal_skin()
        elif name == "skin_electric":
            img = create_electric_spark_skin()
        elif name == "skin_shadow":
            img = create_shadow_wisp_skin()
        else:
            img = create_simple_square_skin(name, colors)

        filepath = os.path.join(OUTPUT_DIR, f"{name}.png")
        img.save(filepath, 'PNG')
        print(f"✓ Generated: {name}.png (50x50px)")

    # Also create higher resolution versions for display
    print("\nGenerating high-res versions for preview (200x200px)...")
    preview_dir = os.path.join(OUTPUT_DIR, "../drawable-xxxhdpi")
    os.makedirs(preview_dir, exist_ok=True)

    for name, colors in SKINS.items():
        if name == "skin_rainbow":
            img = create_rainbow_gradient_skin(200)
        elif name == "skin_fire":
            img = create_fire_animated_skin(200)
        elif name == "skin_ice":
            img = create_ice_crystal_skin(200)
        elif name == "skin_electric":
            img = create_electric_spark_skin(200)
        elif name == "skin_shadow":
            img = create_shadow_wisp_skin(200)
        else:
            img = create_simple_square_skin(name, colors, 200)

        filepath = os.path.join(preview_dir, f"{name}.png")
        img.save(filepath, 'PNG')
        print(f"✓ Generated: {name}.png (200x200px)")

    print("\n" + "=" * 60)
    print(f"✅ All skins generated successfully!")
    print(f"📁 Location: {OUTPUT_DIR}")
    print(f"📁 High-res: {preview_dir}")
    print("\nTo use these in your game, you'll need to:")
    print("1. Update Player.kt to load textures instead of using ShapeRenderer")
    print("2. Add texture disposal in the dispose() method")
    print("3. Update rendering to use SpriteBatch instead of ShapeRenderer")

if __name__ == "__main__":
    main()
