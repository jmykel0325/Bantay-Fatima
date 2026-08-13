<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Mail\Mailable;
use Illuminate\Mail\Mailables\Content;
use Illuminate\Mail\Mailables\Envelope;
use Illuminate\Queue\SerializesModels;

class VerificationCodeMail extends Mailable
{
    use Queueable, SerializesModels;

    public function __construct(public string $code, public string $purpose) {}

    public function envelope(): Envelope
    {
        return new Envelope(subject: $this->purpose === 'registration' ? 'Your Bantay Fatima Verification Code' : 'Your Bantay Fatima Password Reset Code');
    }

    public function content(): Content
    {
        return new Content(view: 'emails.verification-code');
    }
}
