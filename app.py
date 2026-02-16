from flask import Flask, render_template, request, jsonify
from playwright.sync_api import sync_playwright
import pandas as pd
import os
import time

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = 'uploads'
os.makedirs(app.config['UPLOAD_FOLDER'], exist_ok=True)

def check_badges_for_user(username, badge_names):
    """Check which badges a user has completed"""
    url = f"https://www.codecademy.com/users/{username}/achievements"
    results = []
    
    try:
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=True)
            page = browser.new_page()
            page.goto(url, wait_until='networkidle', timeout=30000)
            
            # Wait for content to load
            time.sleep(3)
            
            # Get all badge text on the page
            page_content = page.content().lower()
            
            for badge in badge_names:
                badge_lower = badge.lower().strip()
                completed = badge_lower in page_content
                results.append({
                    'badge': badge,
                    'completed': completed
                })
            
            browser.close()
            
    except Exception as e:
        print(f"Error checking {username}: {e}")
        for badge in badge_names:
            results.append({
                'badge': badge,
                'completed': False
            })
    
    return results

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/check', methods=['POST'])
def check_badges():
    try:
        # Get uploaded files
        usernames_file = request.files['usernames']
        badges_file = request.files['badges']
        
        # Save files temporarily
        usernames_path = os.path.join(app.config['UPLOAD_FOLDER'], 'usernames.csv')
        badges_path = os.path.join(app.config['UPLOAD_FOLDER'], 'badges.csv')
        
        usernames_file.save(usernames_path)
        badges_file.save(badges_path)
        
        # Read CSV files
        usernames_df = pd.read_csv(usernames_path)
        badges_df = pd.read_csv(badges_path)
        
        # Get lists
        usernames = usernames_df.iloc[:, 0].tolist()
        badge_names = badges_df.iloc[:, 0].tolist()
        
        # Check badges for each user
        all_results = []
        for username in usernames:
            user_badges = check_badges_for_user(username, badge_names)
            all_results.append({
                'username': username,
                'badges': user_badges
            })
        
        return jsonify({'success': True, 'results': all_results})
        
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)
