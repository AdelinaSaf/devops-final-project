from flask import Flask, request, send_file, jsonify
from fpdf import FPDF
from fpdf.enums import XPos, YPos
import os
import tempfile

app = Flask(__name__)


@app.route('/generate-pdf', methods=['POST'])
def generate_pdf():
    try:
        recipe = request.json
        ingredients = recipe.get('ingredients', '').split(',') if isinstance(recipe.get('ingredients'),
                                                                             str) else recipe.get('ingredients', [])
        steps = recipe.get('steps', '').split(',') if isinstance(recipe.get('steps'),
                                                                               str) else recipe.get('steps', [])

        # Создаем временный файл для PDF
        pdf_path = os.path.join(tempfile.gettempdir(), "recipe.pdf")

        # Создание PDF с исправлениями
        pdf = FPDF()
        pdf.add_page()

        # Используем стандартный шрифт Helvetica
        pdf.set_font("Helvetica", "B", 16)  # Жирный шрифт для заголовка

        # Заголовок
        title = recipe.get('name', 'Untitled Recipe')
        pdf.cell(0, 10, txt=title, align='C', new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        pdf.ln(10)

        # Ингредиенты
        pdf.set_font("Helvetica", "B", 12)
        pdf.cell(0, 10, txt="Ingredients:", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        pdf.set_font("Helvetica", "", 12)
        for ingredient in ingredients:  # Используем преобразованный список
            pdf.cell(0, 10, txt=f"- {ingredient.strip()}", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        # Инструкции
        pdf.ln(5)
        pdf.set_font("Helvetica", "B", 12)
        pdf.cell(0, 10, txt="Instructions:", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        pdf.set_font("Helvetica", "", 12)
        for i, instruction in enumerate(steps, 1):  # Используем преобразованный список
            pdf.multi_cell(0, 10, txt=f"{i}. {instruction.strip()}", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

        pdf.output(pdf_path)

        return send_file(pdf_path, as_attachment=True, download_name="recipe.pdf")

    except Exception as e:
        app.logger.error(f"Error generating PDF: {str(e)}")
        return jsonify({"error": str(e)}), 500


@app.route('/health')
def health():
    return "OK", 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050)
