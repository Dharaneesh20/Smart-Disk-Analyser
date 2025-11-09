# Smart Disk Analyzer - GitHub Pages Website

This directory contains the GitHub Pages website for Smart Disk Analyzer.

## 🌐 Live Website

The website is hosted at: `https://dharaneesh20.github.io/Smart-Disk-Analyser/`

## 📁 Files

- **index.html** - Main landing page with features, downloads, and documentation
- **styles.css** - Complete styling with responsive design and animations
- **script.js** - JavaScript for GitHub API integration, smooth scrolling, and UI enhancements

## ✨ Features

- **Responsive Design** - Works on desktop, tablet, and mobile devices
- **GitHub Integration** - Automatically fetches latest release information via GitHub API
- **Download Links** - Direct download links for both installer and portable versions
- **Smooth Animations** - Fade-in effects and scroll animations
- **Modern UI** - Clean, professional design with gradient hero section
- **Feature Showcase** - Comprehensive feature list with icons and descriptions
- **Comparison Table** - Side-by-side comparison with AOMEI Partition Assistant
- **Documentation Links** - Quick access to user guides, issues, and contributing guidelines

## 🚀 Deployment to GitHub Pages

### Option 1: Using GitHub Web Interface

1. Go to your repository on GitHub
2. Navigate to **Settings** → **Pages**
3. Under "Source", select **Deploy from a branch**
4. Select branch: `main`
5. Select folder: `/docs`
6. Click **Save**
7. Wait 2-3 minutes for deployment
8. Your site will be live at: `https://dharaneesh20.github.io/Smart-Disk-Analyser/`

### Option 2: Using Git Commands

```powershell
# Make sure all docs files are committed
git add docs/
git commit -m "Add GitHub Pages website"
git push origin main

# Then enable GitHub Pages in repository settings as described above
```

## 🔄 Updating the Website

When you create a new release on GitHub:

1. The website will **automatically** detect and display the new version
2. Download links will **automatically** update to point to the latest release
3. No need to manually edit the HTML!

### Manual Updates

If you need to update content:

1. Edit `index.html` - Update features, text, or structure
2. Edit `styles.css` - Update colors, layout, or styling
3. Edit `script.js` - Update functionality or API integration
4. Commit and push changes:

```powershell
git add docs/
git commit -m "Update website content"
git push origin main
```

Changes will be live within 2-3 minutes.

## 🎨 Customization

### Change Colors

Edit CSS variables in `styles.css`:

```css
:root {
    --primary-color: #2563eb;     /* Main blue color */
    --primary-dark: #1e40af;      /* Darker blue for hover */
    --accent-color: #10b981;      /* Green for highlights */
    /* ... more variables ... */
}
```

### Update Repository Name

If you change the repository name, update in `script.js`:

```javascript
const GITHUB_REPO = 'Dharaneesh20/Smart-Disk-Analyser';
```

### Add Logo

1. Add a `logo.png` file to the `/docs` folder
2. The navbar will automatically display it (32x32px or 40x40px recommended)

### Add Favicon

1. Add a `favicon.png` file to the `/docs` folder (32x32px recommended)
2. Browsers will automatically use it

## 📱 Testing Locally

Open `index.html` directly in a browser:

```powershell
# Open in default browser
start docs/index.html

# Or right-click index.html and select "Open with" → your browser
```

For full GitHub API testing, use a local server:

```powershell
# Option 1: Python
python -m http.server 8000 --directory docs

# Option 2: Node.js (if you have http-server)
npx http-server docs -p 8000

# Then open: http://localhost:8000
```

## 🐛 Troubleshooting

### Downloads Not Working

- **Cause**: No releases created yet on GitHub
- **Solution**: Create a release with installer and portable .exe files
- **Fallback**: Website redirects to GitHub Releases page

### Version Not Updating

- **Cause**: GitHub API rate limiting or cache
- **Solution**: Wait a few minutes or clear browser cache (Ctrl+F5)

### Styling Issues

- **Cause**: CSS not loading or browser cache
- **Solution**: Hard refresh (Ctrl+Shift+R) or check browser console for errors

### GitHub Pages Not Loading

- **Cause**: Not enabled in repository settings
- **Solution**: Go to Settings → Pages and select `/docs` folder
- **Note**: Repository must be public for free GitHub Pages

## 📊 Analytics (Optional)

To add Google Analytics:

1. Get your Google Analytics tracking ID
2. Add to `index.html` before `</head>`:

```html
<!-- Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=YOUR-GA-ID"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'YOUR-GA-ID');
</script>
```

3. Update `script.js` to track downloads (already implemented)

## 🔒 Security

- All external links have `rel="noopener noreferrer"` for security
- No user data is collected or stored
- GitHub API calls are read-only
- No cookies or tracking (unless you add analytics)

## 📄 License

This website is part of the Smart Disk Analyzer project and follows the same license (MIT).

## 🤝 Contributing

To improve the website:

1. Fork the repository
2. Edit files in `/docs` folder
3. Test locally
4. Submit a pull request

---

**Website Created By**: Dharaneesh20  
**Last Updated**: November 2025  
**Framework**: Vanilla HTML/CSS/JavaScript (No dependencies!)
