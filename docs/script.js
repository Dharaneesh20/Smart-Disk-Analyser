// GitHub Repository Configuration
const GITHUB_REPO = 'Dharaneesh20/Smart-Disk-Analyser';
const GITHUB_API = `https://api.github.com/repos/${GITHUB_REPO}`;

// Download URLs will be fetched from GitHub Releases
let latestRelease = null;

/**
 * Fetch the latest release information from GitHub
 */
async function fetchLatestRelease() {
    try {
        const response = await fetch(`${GITHUB_API}/releases/latest`);
        if (!response.ok) {
            throw new Error('Failed to fetch release information');
        }
        latestRelease = await response.json();
        updateVersionInfo();
        updateDownloadLinks();
    } catch (error) {
        console.error('Error fetching release:', error);
        // Fallback to manual links if API fails
        setupFallbackLinks();
    }
}

/**
 * Update version information on the page
 */
function updateVersionInfo() {
    if (!latestRelease) return;

    const version = latestRelease.tag_name.replace('v', '');
    document.getElementById('installer-version').textContent = version;
    document.getElementById('portable-version').textContent = version;
    document.getElementById('deb-version').textContent = version;
}

/**
 * Update download button links with actual release assets
 */
function updateDownloadLinks() {
    if (!latestRelease || !latestRelease.assets) return;

    const assets = latestRelease.assets;
    
    // Find installer and portable executables
    const installer = assets.find(asset => 
        asset.name.includes('Setup') && asset.name.endsWith('.exe')
    );
    
    const portable = assets.find(asset => 
        !asset.name.includes('Setup') && 
        asset.name.endsWith('.exe') && 
        asset.name.includes('Smart Disk Analyzer')
    );

    // Find Debian package
    const debPackage = assets.find(asset => 
        asset.name.endsWith('.deb')
    );

    // Update download buttons
    if (installer) {
        const installerBtn = document.getElementById('download-installer');
        installerBtn.href = installer.browser_download_url;
        installerBtn.onclick = null; // Remove fallback onclick
    }

    if (portable) {
        const portableBtn = document.getElementById('download-portable');
        portableBtn.href = portable.browser_download_url;
        portableBtn.onclick = null; // Remove fallback onclick
    }

    if (debPackage) {
        const debBtn = document.getElementById('download-deb');
        debBtn.href = debPackage.browser_download_url;
        debBtn.onclick = null; // Remove fallback onclick
    }
}

/**
 * Setup fallback links if GitHub API is unavailable
 */
function setupFallbackLinks() {
    const installerBtn = document.getElementById('download-installer');
    const portableBtn = document.getElementById('download-portable');
    
    // Point to releases page as fallback
    const releasesUrl = `https://github.com/${GITHUB_REPO}/releases/latest`;
    installerBtn.href = releasesUrl;
    portableBtn.href = releasesUrl;
    
    installerBtn.onclick = null;
    portableBtn.onclick = null;
}

/**
 * Handle download button clicks (fallback for when API hasn't loaded)
 */
function downloadRelease(type) {
    if (latestRelease && latestRelease.assets) {
        // If release info is loaded, the href should already be set
        return true;
    }
    
    // Fallback: redirect to releases page
    window.open(`https://github.com/${GITHUB_REPO}/releases/latest`, '_blank', 'noopener,noreferrer');
    return false;
}

/**
 * Smooth scroll for anchor links
 */
function setupSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const href = this.getAttribute('href');
            if (href === '#') return;
            
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Add animation on scroll
 */
function setupScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    // Observe all feature cards and doc cards
    document.querySelectorAll('.feature-card, .doc-card, .download-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease-out, transform 0.6s ease-out';
        observer.observe(card);
    });
}

/**
 * Track download events (optional - for analytics)
 */
function trackDownload(type) {
    // You can integrate Google Analytics or other analytics here
    console.log(`Download initiated: ${type}`);
    
    // Example: Google Analytics event tracking
    if (typeof gtag !== 'undefined') {
        gtag('event', 'download', {
            'event_category': 'Downloads',
            'event_label': type,
            'value': 1
        });
    }
}

/**
 * Add rel="noopener" to external links for security
 */
function secureExternalLinks() {
    document.querySelectorAll('a[target="_blank"]').forEach(link => {
        const currentRel = link.getAttribute('rel') || '';
        if (!currentRel.includes('noopener')) {
            link.setAttribute('rel', currentRel + ' noopener noreferrer');
        }
    });
}

/**
 * Show loading state on download buttons
 */
function showDownloadLoading(button) {
    button.classList.add('loading');
    setTimeout(() => {
        button.classList.remove('loading');
    }, 2000);
}

/**
 * Setup download button click handlers
 */
function setupDownloadButtons() {
    const installerBtn = document.getElementById('download-installer');
    const portableBtn = document.getElementById('download-portable');
    const debBtn = document.getElementById('download-deb');

    installerBtn.addEventListener('click', function(e) {
        showDownloadLoading(this);
        trackDownload('installer');
    });

    portableBtn.addEventListener('click', function(e) {
        showDownloadLoading(this);
        trackDownload('portable');
    });

    debBtn.addEventListener('click', function(e) {
        showDownloadLoading(this);
        trackDownload('deb');
    });
}

/**
 * Show/hide platform-specific requirements
 */
function showPlatform(platform) {
    // Update tab active state
    const tabs = document.querySelectorAll('.platform-tab');
    tabs.forEach(tab => tab.classList.remove('active'));
    event.target.classList.add('active');

    // Show/hide requirements sections
    const windowsReqs = document.getElementById('windows-requirements');
    const linuxReqs = document.getElementById('linux-requirements');

    if (platform === 'windows') {
        windowsReqs.classList.remove('hidden');
        linuxReqs.classList.add('hidden');
    } else if (platform === 'linux') {
        windowsReqs.classList.add('hidden');
        linuxReqs.classList.remove('hidden');
    }
}

/**
 * Display error message if releases can't be loaded
 */
function showReleaseError() {
    const downloadSection = document.getElementById('download');
    const errorMsg = document.createElement('div');
    errorMsg.style.cssText = 'background: #fee; border: 1px solid #fcc; padding: 1rem; border-radius: 8px; margin: 1rem auto; max-width: 600px; text-align: center;';
    errorMsg.innerHTML = `
        <p style="color: #c33; margin: 0;">
            <strong>Note:</strong> Unable to fetch latest release. 
            Please visit our <a href="https://github.com/${GITHUB_REPO}/releases" target="_blank" rel="noopener noreferrer" style="color: #c33; text-decoration: underline;">GitHub Releases page</a> to download.
        </p>
    `;
    downloadSection.insertBefore(errorMsg, downloadSection.querySelector('.download-cards'));
}

/**
 * Check for system compatibility
 */
function checkSystemCompatibility() {
    const userAgent = window.navigator.userAgent;
    const isWindows = userAgent.includes('Windows');
    
    if (!isWindows) {
        const warningMsg = document.createElement('div');
        warningMsg.style.cssText = 'background: #fff3cd; border: 1px solid #ffc107; padding: 1rem; border-radius: 8px; margin: 2rem auto; max-width: 800px; text-align: center;';
        warningMsg.innerHTML = `
            <p style="color: #856404; margin: 0;">
                ⚠️ <strong>Note:</strong> Smart Disk Analyzer is currently only available for Windows 10/11 (x64). 
                Your system appears to be ${userAgent.includes('Mac') ? 'macOS' : userAgent.includes('Linux') ? 'Linux' : 'unknown'}.
            </p>
        `;
        const hero = document.querySelector('.hero .container');
        hero.appendChild(warningMsg);
    }
}

/**
 * Add keyboard navigation support
 */
function setupKeyboardNavigation() {
    document.addEventListener('keydown', function(e) {
        // Press 'D' to scroll to download section
        if (e.key === 'd' || e.key === 'D') {
            if (!e.ctrlKey && !e.altKey && document.activeElement.tagName !== 'INPUT') {
                e.preventDefault();
                document.getElementById('download').scrollIntoView({ behavior: 'smooth' });
            }
        }
    });
}

/**
 * Initialize the page
 */
async function init() {
    console.log('Initializing Smart Disk Analyzer website...');
    
    // Setup UI enhancements
    setupSmoothScroll();
    setupScrollAnimations();
    secureExternalLinks();
    setupDownloadButtons();
    setupKeyboardNavigation();
    checkSystemCompatibility();
    
    // Fetch and update release information
    try {
        await fetchLatestRelease();
        console.log('Release information loaded successfully');
    } catch (error) {
        console.error('Failed to load release information:', error);
        showReleaseError();
    }
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}

// Export functions for inline use
window.downloadRelease = downloadRelease;
window.showPlatform = showPlatform;
